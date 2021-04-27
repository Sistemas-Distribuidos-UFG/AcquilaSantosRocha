from random import choice

import logging

logger = logging.getLogger(__name__)

class Properties:

    colors = ["blue", "red", "green", "yellow"]
    card_types = ["color_changer", "pick_four", "picker", "skip", "reverse", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]

    def __init__(self, filename:str=None):
        self.color:str = None
        self.type:str = None
        self.filename:str = filename
        if not filename:
            self._getRandom()

    def _getRandomColor(self):
        self.color = choice(Properties.colors)

    def _getRandomType(self):
        self.type = choice(Properties.card_types)
    
    @staticmethod
    def generateFilename(color, ctype) -> str:
        res = "small_cards/"
        if ctype not in ["color_changer", "pick_four"]:
            res += color + "_" + ctype + ".png"
        else:
            res += "wild_" + ctype + ".png"
        
        return res

    def _getRandom(self) -> tuple:
        self._getRandomType()
        if self.type not in ["color_changer", "pick_four"]:
            self._getRandomColor()

        self.filename = Properties.generateFilename(self.color, self.type)
        
        logger.debug("Generated card of type {} and color {} - {}".format(self.type, self.color, self.filename))

        # return (self.type, self.color)
