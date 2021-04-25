import logging

from uno.objects.deck import Deck

logger = logging.getLogger(__name__)

class Player:
    def __init__(self, player_id, host="localhost"):
        self.id = player_id
        self.host = host
        logger.info("Generating player {}".format(self.id))
        self.hand = Deck(7, player_id)
        self.skip = False

    def play(self, card_id):
        card = self.hand.playCardFromHand(card_id)
        logger.info("Played ({},{},{})".format(card.id, card.properties.type, card.properties.color))
    
    def grabCards(self, number=1):
        self.hand.getCardFromDeck(number)
        
    
