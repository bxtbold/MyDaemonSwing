#!/usr/bin/env python

import json
import socket
import sys

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn


def target_action(ip="192.168.56.1", port="9090"):
    """Get the target action from the ROS topic."""
    sys.stderr.write("[DAEMON] target_action function is running.")
    try:
        # Create a TCP/IP socket and connect it to the server's address and port
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((ip, int(port)))
        # send json to rosbridge
        target_action = send_ros(sock, "call_service", "/plaif_vision_pick/target_action")
        sock.close()
        return target_action
    except socket.error as e:
        sys.stderr.write("[DAEMON] Communication failed! Check if rosbridge is running or check ip address! Error message: " + str(e))
        return -2
    except ValueError as e:
        sys.stderr.write("[DAEMON] Please check if the port is correct: " + str(port))
        return -3
    except Exception as e:
        return -4


def send_ros(sock, op, topic_name, value_name='target_action'):
    data = build_json(op, topic_name)
    data_encoded = json.dumps(data).encode("utf-8")
    sock.sendall(data_encoded)
    if op == "publish":
        send_ros(sock, "advertise", topic_name)
        return 1
    if op == "call_service":
        while True:
            response = sock.recv(1024)
            if response:
                break
        result = json.loads(response)
        return int(result['values'][value_name])
    return None


# build json for rosbridge
def build_json(op, topic_name, msg_type="std_msgs/Int8"):
    if op == "advertise":
        data = {"op": op, "topic":topic_name, "type":msg_type}
    elif op == "publish":
        data = {"op": op, "topic":topic_name, "msg":{}}
    elif op == "call_service":
        data = {"op": op, "service":topic_name, "args":{}}
    return data


class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 40405))
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(target_action, "target_action")
server.serve_forever()
