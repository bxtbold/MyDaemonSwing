import socket
import struct

class RobotRealtimeReader:
    def __init__(self, IP='127.0.0.1', port=30003):
        self.TCP_IP = IP
        self.TCP_port = port
        self.RealtimeMessage = []

    def readNow(self):
        self.readSocket()
        self.getActualJointPose()

    def readSocket(self):
        # try:
        if True:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as rt:
                rt.connect((self.TCP_IP, self.TCP_port))
                print('Connected to UR Realtime Client')

                with rt.makefile('rb') as in_file:
                    # Read the integer available in stream
                    length = struct.unpack('>i', in_file.read(4))[0]
                    print('Length is ', length)

                    # Initialize size of RealtimeMessage by using received length
                    self.RealtimeMessage = [0] * length
                    # Add length integer to output array
                    self.RealtimeMessage[0] = length

                    # Calculate how much data is available from the length
                    data_available = (length-4)//8
                    print('There are ', data_available, ' doubles available')

                    # Loop over reading the available data
                    for i in range(1, data_available+1):
                        self.RealtimeMessage[i] = struct.unpack('>d', in_file.read(8))[0]
                        print('Index ', i, ' is ', self.RealtimeMessage[i])

                # Perform housekeeping
                rt.shutdown(socket.SHUT_RDWR)
                print('Disconnected from UR Realtime Client')
        # except Exception as e:
        #     print('-------------------')
        #     print(e)

    class RTinfo:
        # name            (index in plot, number of doubles)
        q_target        = (2, 6)
        qd_target       = (8, 6)
        qdd_target      = (14, 6)
        q_actual        = (32, 6)
        qd_actual       = (38, 6)
        TCP_actual      = (56, 6)
        TCPd_actual     = (62, 6)
        TCP_force       = (68, 6)
        TCP_target      = (74, 6)
        TCPd_target     = (80, 6)
        temp_joint      = (87, 6)
        robotmode       = (95, 1)
        jointmode       = (96, 6)
        safetymode      = (97, 1)
        tcp_accel       = (109, 3)
        speedscaling    = (118, 1)
        prgstate        = (132, 1)

        def __init__(self, index, count):
            self.index = index
            self.count = count

    def getActualJointPose(self):
        val = [0] * 6
        print(self.RealtimeMessage)
        # for i in range(len(val)):
        #     val[i] = self.RealtimeMessage[33]
        return val


if __name__ == "__main__":
    reader = RobotRealtimeReader()
    print(reader.readNow())
