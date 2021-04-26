from uno.objects.card import Card

class Board:

    def __init__(self, name):
        self.name = name
        self.cards:dict = {}
        self.top:Card = None
        self.turn_iterator = 1

    def update_Board(self, card:Card):
        self.cards[card.id] = card
        self.top = card