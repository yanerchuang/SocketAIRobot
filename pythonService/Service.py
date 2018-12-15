import socketserver


class Myserver(socketserver.BaseRequestHandler):

    def handle(self):

        conn = self.request
        conn.sendall(bytes("你好，我是人工智能机器人！有什么需要帮助的吗？", encoding="utf-8"))
        while True:
            ret_bytes = conn.recv(1024)
            ret_str = str(ret_bytes, encoding="utf-8")
            print('【收到消息】：' + ret_str)
            if ret_str == "q" or ret_str == '':
                break
            ret_str = ret_str.replace('吗', '')
            ret_str = ret_str.replace('？', '！')
            conn.sendall(bytes(ret_str, encoding="utf-8"))


if __name__ == "__main__":
    # server = socketserver.ThreadingTCPServer(("127.0.0.1",8899),Myserver)
    server = socketserver.ThreadingTCPServer(("172.27.23.1", 8899), Myserver)
    server.serve_forever()
