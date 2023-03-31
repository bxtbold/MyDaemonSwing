#!/usr/bin/env python

import json
import socket
import sys

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

title = ""

# publish topic on a given topic name
def capture(ip="192.168.56.1", port="9090"):
    sys.stderr.write("[DAEMON] Capture function is running.")
    try:
        # Create a TCP/IP socket and connect it to the server's address and port
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((ip, int(port)))
        # send json to rosbridge
        result = send_ros(sock, "call_service", "/plaif_vision_pick/target_action")
        sock.close()
        return result
    except socket.error as e:
        sys.stderr.write("[DAEMON] Communication failed! Check if rosbridge is running or check ip address! Error message: " + str(e))
        return -2
    except ValueError as e:
        sys.stderr.write("[DAEMON] Please check if the port is correct: " + str(port))
        return -3

def send_ros(sock, op, topic_name):
    data = build_json(op, topic_name)
    data_encoded = json.dumps(data).encode("utf-8")
    sock.sendall(data_encoded)
    if op == "publish":
        send_ros(sock, "advertise", topic_name)
    if op == "call_service":
        while True:
            response = sock.recv(1024)
            if response:
                break
        result = json.loads(response)
        return result['values']['target_action']
    return 1

# build json for rosbridge
def build_json(op, topic_name, msg_type="std_msgs/Int8"):
    if op == "advertise":
        data = {"op": op, "topic":topic_name, "type":msg_type}
    elif op == "publish":
        data = {"op": op, "topic":topic_name, "msg":{}}
    elif op == "call_service":
        data = {"op": op, "service":topic_name, "args":{}}
    return data

def set_title(new_title):
	global title
	title = new_title
	return title

def get_title():
	tmp = ""
	if str(title):
		tmp = title
	else:
		tmp = "No title set"
	return tmp + " (Python)"

def get_message(name):
	if str(name):
		return "Hello " + str(name) + ", welcome to PolyScope!"
	else:
		return "No name set"

sys.stdout.write("Python Server daemon started.")
sys.stderr.write("Python Server daemon started.")

class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 40405))
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(set_title, "set_title")
server.register_function(get_title, "get_title")
server.register_function(get_message, "get_message")
server.register_function(capture, "capture")
server.serve_forever()
