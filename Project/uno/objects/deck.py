import logging

from uno.objects.card import Card

logger = logging.getLogger(__name__)


class Deck:
    def __init__(self, size, player_id):
        self.cards:dict = {}
        self.owner = player_id
        self.size:int = size
        self.generateRandomHand()
        
    def cardsList(self) -> list:
        return list(self.cards.values())

    def generateRandomHand(self) -> None:
        for _ in range(self.size):
            card = Card()
            self.cards[card.id] = card
    
    def playCardFromHand(self, cid) -> Card:
        if cid in self.cards:
            card = self.cards[cid]
            self.cards.pop(cid, None)
            return card
            
        return None
    
    def getCardFromDeck(self, number = 1):
        for _ in range(number):
            card = Card()
            self.cards[card.id] = card
    
