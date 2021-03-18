import json 
from defs import Problem, sNL

class Message:
    def __init__(self, problem:Problem=None, questionData:dict={}):
        self.pType:Problem = problem
        self.data:dict = questionData

        self.msgDict:dict = {'problem': self.pType, 'data': self.data}

    def serialize(self) -> str:
        if not self.msgDict:
            return ''
        return json.dumps(self.msgDict)

    def build(self, msg:str):
        if not msg:
            return ''

        self.msgDict:dict = json.loads(msg)
        self.pType = self.msgDict['problem']
        self.data = self.msgDict['data']
        

    @staticmethod
    def deserialize(msg:str) -> dict:
        if not msg:
            return ''
        return json.loads(msg)
    

    @staticmethod
    def serialize(problem:Problem, msg:str) -> str:
        if not msg:
            return ''

        return json.dumps({'problem': str(problem) ,'data': msg})

    @staticmethod
    def getProblem(msg:str) -> Problem:
        if not msg:
            return Problem.nda
        
        msg_str = json.loads(msg)
        problem_number = msg_str['problem']
        return Problem(int(problem_number))

if __name__ == '__main__':
    

    m = Message()

    m.build( json.dumps({'problem': 1, 'data': {'a': 'b'}}) )
    
    slogger = sNL.get_logger()

    slogger.info(m.pType)