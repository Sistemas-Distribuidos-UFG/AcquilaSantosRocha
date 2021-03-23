import os, sys, socket, json
import threading

from defs import Problem, sNL
from message import Message

class Solve:
	def __init__(self, pType:Problem, data:dict):
		self.pType = pType
		self.data = json.loads(data)

	def q1(self) -> dict:
		res:dict = {}
		if self.data['cargo'] == 'operador':
			res = {'nome': self.data['nome'], 'salario': float(self.data['salario'])*1.2}
		elif self.data['cargo'] == 'programador':
			res = {'nome': self.data['nome'], 'salario': float(self.data['salario'])*1.18}
		else:
			res = {'nome': self.data['nome'], 'salario': float(self.data['salario'])}

		return res

	def q2(self) -> dict:
		res:dict = {}
		if (self.data['sexo'] == 'masculino' and int(self.data['idade']) >= 18) or (self.data['sexo'] == 'feminino' and int(self.data['idade']) >= 21):
			res = {'nome': self.data['nome'], 'maioridade': True}
		else:
			res = {'nome': self.data['nome'], 'maioridade': False}

		return res

	def q3(self) -> dict:
		res:dict = {}
		m = (float(self.data['N1']) + float(self.data['N2']))/2.0
		if (m >= 7.0) or ((m > 3.0 and m < 7.0) and (m + float(self.data['N3']))/2.0 >= 5.0):
			res = {'aprovado': True}
		else:
			res = {'aprovado': False}

		return res

	def q4(self) -> dict:
		res:dict = {}
		if self.data['sexo'] == 'masculino':
			res = {'sexo': self.data['sexo'], 'peso': 72.7 * float(self.data['altura']) - 58}
		else:
			res = {'sexo': self.data['sexo'], 'peso': 62.1 * float(self.data['altura']) - 44.7}

		return res

	def q5(self) -> dict:
		res:dict = {}
		idade = int(self.data['idade'])
		if idade >= 5 and idade <= 7:
			res = {'idade': idade, 'categora': 'INFANTIL A'}
		elif idade >= 8 and idade <= 10:
			res = {'idade': idade, 'categora': 'INFANTIL B'}
		elif idade >= 11 and idade <= 13:
			res = {'idade': idade, 'categora': 'JUVENIL A'}
		elif idade >= 14 and idade <= 17:
			res = {'idade': idade, 'categora': 'JUVENIL B'}
		elif idade >= 18 :
			res = {'idade': idade, 'categora': 'ADULTO'}
		else:
			res = {'idade': idade, 'categora': 'inapto'}

		return res

	def q6(self) -> dict:
		res:dict = {}
		nome = self.data['nome']
		nivel = self.data['nivel']
		salario = float(self.data['salario'])
		dependentes = int(self.data['dependentes'])

		if nivel == 'A':
			res = {'nome': self.data['nome'], 'liquido': salario*0.97 if dependentes == 0 else salario*0.92}
		elif nivel == 'B':
			res = {'nome': self.data['nome'], 'liquido': salario*0.95 if dependentes == 0 else salario*0.90}
		elif nivel == 'C':
			res = {'nome': self.data['nome'], 'liquido': salario*0.92 if dependentes == 0 else salario*0.85}
		elif nivel == 'D':
			res = {'nome': self.data['nome'], 'liquido': salario*0.90 if dependentes == 0 else salario*0.83}
		else:
			res = {'nome': self.data['nome'], 'liquido': salario}

		return res

	def q7(self) -> dict:
		res:dict = {}
		idade = int(self.data['idade'])
		tempo = int(self.data['tempo'])

		if (idade >= 65 and tempo >= 30) or (idade >= 60 and tempo >= 25):
			res = {'idade': idade, 'tempo': tempo, 'aposenta': True}
		else:
			res = {'idade': idade, 'tempo': tempo, 'aposenta': False}

		return res

	def q8(self) -> dict:
		res:dict = {}
		saldo = float(self.data['saldo'])

		if saldo >= 0 and saldo <= 200:
			res = {'saldo': saldo, 'credito': 'Nenhum'}
		elif saldo >= 201 and saldo <= 400:
			res = {'saldo': saldo, 'credito': saldo*0.2}
		elif saldo >= 401 and saldo <= 600:
			res = {'saldo': saldo, 'credito': saldo*0.3}
		elif saldo >= 601:
			res = {'saldo': saldo, 'credito': saldo*0.4}

		return res


	def q9(self) -> dict:
		res:dict = {}
		valor = int(self.data['valor'])
		naipe = int(self.data['naipe'])

		res = {'valor': valor, 'naipe': naipe}

		return res


class Server:

	def __init__(self, host):
		self.host = host
		self.port = 54321

		self.serverSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self.serverSock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
		self.serverSock.bind((self.host , self.port))
		self.serverSock.listen(100)

		self.clients = []

		self.slogger = sNL.get_logger()

	def _solve(self, pType:Problem, data:dict):

		solver:Solve = Solve(pType, data)
		res:dict = {}
		
		self.slogger.info("Solving task {}".format(pType))

		if pType == 'Problem.q1':
			res = solver.q1()
		elif pType == 'Problem.q2':
			res = solver.q2()
		elif pType == 'Problem.q3':
			res = solver.q3()
		elif pType == 'Problem.q4':
			res = solver.q4()
		elif pType == 'Problem.q5':
			res = solver.q5()
		elif pType == 'Problem.q6':
			res = solver.q6()
		elif pType == 'Problem.q7':
			res = solver.q7()
		elif pType == 'Problem.q8':
			res = solver.q8()
		elif pType == 'Problem.q9':
			res = solver.q9()
		else:
			raise NotImplemented

		self.slogger.debug("Response {}".format(res))

		return json.dumps(res)

	def _communicate(self, conn, addr) -> None:

		while True:
			msg = conn.recv(1024)
			if msg:
				self.slogger.info("{} sent a task".format(addr[0]))
				m = Message()
				m.build(msg)
				if m.data == 'disconnect':
					self.closeConnection(conn)
					self.slogger.info("{} disconnected!".format(addr[0]))
					break
				conn.send(Message.serialize(m.pType, self._solve(m.pType, m.data)).encode())
			elif conn in self.clients:
				self.clients.remove(conn)
		return

	def run(self):
		self.slogger.info("Server accepting connections!")
		self.slogger.debug("Host: {} Port: {}".format(self.host, self.port))
		while True:
			conn, addr = self.serverSock.accept()
			self.slogger.info("{} connected!".format(addr[0]))
			self.clients.append(conn)
			threading.Thread(target=self._communicate, args=[conn, addr]).start()

	def closeConnection(self, conn):
		conn.close()
		self.clients.remove(conn)
		# self.serverSock.close()
			
if __name__ == '__main__':
	
	s = Server('localhost')
	s.run()