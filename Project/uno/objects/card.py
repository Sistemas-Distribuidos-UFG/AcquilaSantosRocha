import pygame
import random

from uno.objects.properties import Properties

import logging

logger = logging.getLogger(__name__)


class Card:
    def __init__(self, name="", filename="", owner=None):
        self.name = name
        self.id = str(random.getrandbits(64))
        self.properties:Properties = None
        self.data:Surface = None
        self.rect:Rect = None
        if not filename:
            self.getRandomCard()
        else:
            self.filename = filename

    def getRandomCard(self):
        logger.debug("Creating card {}".format(self.id))
        self.properties = Properties()
        self.data = pygame.image.load(self.properties.filename)
        self.rect = self.data.get_rect()

    def getId(self):
        return self.id

    def getData(self):
        return self.data

    def getProperties(self) -> Properties:
        return self.properties
    
    def canPlay(self, color, ctype) -> bool:
        if (self.properties.color == color or self.properties.type == ctype):
            return True
        return False

    def canPlayWild(self) -> bool:
        if self.properties.color == None:
            return True
        return False