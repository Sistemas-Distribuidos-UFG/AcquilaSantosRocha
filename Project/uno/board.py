from uno.objects.card import Card

class Board:

    def __init__(self, name):
        self.name = name
        self.cards:dict = {}
        self.top:Card = None
        self.turn_iterator = 1
        self.type = None
        self.color = None

    def updateBoard(self, card:Card):
        self.cards[card.id] = card
        self.top = card
        self.type = card.properties.type
        self.color = card.properties.color