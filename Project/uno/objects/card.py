import pygame
import random

from uno.objects.properties import Properties

import logging

logger = logging.getLogger(__name__)


class Card:
    def __init__(self):
        self.id = str(random.getrandbits(64))
        self.properties:Properties = None
        self.data:Surface = None
        self.rect = None
        self.getRandomCard()

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
