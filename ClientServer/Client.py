import os, sys, socket, select, json, argparse
import threading

from defs import Problem, sNL
from message import Message


class Client:
	def __init__(self, host:str, problem:int):
		super().__init__()

		self.host = host
		self.port = 54321
		
		self.problem = Problem(problem)
		self.recvMessage = Message()

		self.serverSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self.serverSock.connect((self.host, self.port))
		
		self.slogger = sNL.get_logger()

		self.slogger.debug("Connected to (host {}, port {})".format(self.host, self.port))
		self.slogger.info("Client to {}".format(self.problem))

	def getProblemNumber(self):
		raise NotImplemented

	def _gatherData(self) -> None:
		raise NotImplemented

	def run(self) -> None:
		self.slogger.info("Client Running!")
		try:
			while True:
				self.serverSock.send(Message.serialize( self._getProblemNumber(), self._gatherData()).encode())
				msg = self.serverSock.recv(1024)
				self.recvMessage.build(msg)
				self.slogger.info("Response received")
				print("[Server] {}".format(self.recvMessage.data))
		except:
			self.serverSock.send(Message.serialize(self._getProblemNumber(), 'disconnect').encode())
			print('\nBye!')


	def close(self) -> None:
		self.serverSock.close()
		


class Question1(Client):
	def __init__(self, host:str):
		super().__init__(host, 1)

	def _getProblemNumber(self):
		return Problem(1);

	def _gatherData(self) -> str:
		nome = input("[Nome] ")
		cargo = input("[Cargo] ")
		salario = float(input("[Salário] "))
		data = {'nome': nome.lower(), 'cargo': cargo, 'salario': salario}
		return json.dumps(data)
			

class Question2(Client):
	def __init__(self, host:str):
		super().__init__(host, 2)

	
	def _getProblemNumber(self):
		return Problem(2);

	def _gatherData(self) -> str:
		nome = input("[Nome] ")
		sexo = input("[Sexo] ")
		idade = int(input("[Idade] "))
		data = {'nome': nome.lower(), 'sexo': sexo, 'idade': idade}
		return json.dumps(data)

class Question3(Client):
	def __init__(self, host:str):
		super().__init__(host, 3)

	
	def _getProblemNumber(self):
		return Problem(3);

	def _gatherData(self) -> str:
		n1 = float(input("[N1] "))
		n2 = float(input("[N2] "))
		n3 = float(input("[N3] "))
		data = {'N1': n1, 'N2': n2, 'N3': n3}
		return json.dumps(data)
			
class Question4(Client):
	def __init__(self, host:str):
		super().__init__(host, 4)

	
	def _getProblemNumber(self):
		return Problem(4);

	def _gatherData(self) -> str:
		altura = float(input("[Altura] "))
		sexo = input("[Sexo] ")
		data = {'altura': altura, 'sexo': sexo}
		return json.dumps(data)


class Question5(Client):
	def __init__(self, host:str):
		super().__init__(host, 5)

	
	def _getProblemNumber(self):
		return Problem(5);

	def _gatherData(self) -> str:
		idade = int(input("[Idade] "))
		data = {'idade': idade}
		return json.dumps(data)
			

class Question6(Client):
	def __init__(self, host:str):
		super().__init__(host, 6)

	
	def _getProblemNumber(self):
		return Problem(6);

	def _gatherData(self) -> str:
		nome = input("[Nome] ")
		nivel = input("[Nível] ")
		salario = float(input("[Salário] "))
		dependentes = input("[Dependentes] ")
		data = {'nome': nome.lower(), 'nivel': nivel, 'salario': salario, 'dependentes': dependentes}
		return json.dumps(data)

class Question7(Client):
	def __init__(self, host:str):
		super().__init__(host, 7)

	
	def _getProblemNumber(self):
		return Problem(7);

	def _gatherData(self) -> str:
		idade = int(input("[Idade] "))
		tempo = int(input("[Tempo de Serviço] "))
		data = {'idade': idade, 'tempo': tempo}
		return json.dumps(data)

class Question8(Client):
	def __init__(self, host:str):
		super().__init__(host, 8)

	
	def _getProblemNumber(self):
		return Problem(8);

	def _gatherData(self) -> str:
		saldo = float(input("[Saldo] "))
		data = {'saldo': saldo}
		return json.dumps(data)

class Question9(Client):
	def __init__(self, host:str):
		super().__init__(host, 9)

	
	def _getProblemNumber(self):
		return Problem(9);

	def _gatherData(self) -> str:
		valor = int(input("[Valor] "))
		naipe = int(input("[Naipe] "))
		data = {'valor': valor, 'naipe': naipe}
		return json.dumps(data)

if __name__ == '__main__':
	
	parser = argparse.ArgumentParser()

	parser.add_argument('-p', '--problem', type=int, help='Problem type [1..9]')

	args = parser.parse_args()

	if args.problem == 1:
		s = Question1('localhost')
		s.run()
	elif args.problem == 2:
		s = Question2('localhost')
		s.run()
	elif args.problem == 3:
		s = Question3('localhost')
		s.run()
	elif args.problem == 4:
		s = Question4('localhost')
		s.run()
	elif args.problem == 5:
		s = Question5('localhost')
		s.run()
	elif args.problem == 6:
		s = Question6('localhost')
		s.run()
	elif args.problem == 7:
		s = Question7('localhost')
		s.run()
	elif args.problem == 8:
		s = Question8('localhost')
		s.run()
	elif args.problem == 9:
		s = Question9('localhost')
		s.run()
	else:
		parser.print_help()
	