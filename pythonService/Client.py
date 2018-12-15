import socket

client = socket.socket()

obj.connect(("127.0.0.1",8899))

ret_bytes = client.recv(1024)
ret_str = str(ret_bytes,encoding="utf-8")
print('【收到服务端消息】：'+ret_str)

while True:
    inp = input('【请输入问题消息】：')
    if inp == "q":
        client.sendall(bytes(inp,encoding="utf-8"))
        break
    else:
        client.sendall(bytes(inp, encoding="utf-8"))
        ret_bytes = client.recv(1024)
        ret_str = str(ret_bytes,encoding="utf-8")
        print('【收到服务端消息】：'+ret_str)