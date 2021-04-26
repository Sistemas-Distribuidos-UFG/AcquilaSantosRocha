import logging

from uno.objects.deck import Deck
from uno.objects.card import Card
from uno.board import Board

from uno.pygame.display import *
from uno.pygame.controls import player_LR_selection_hand

logger = logging.getLogger(__name__)

class Player:
    def __init__(self, player_id, host="localhost"):
        self.id = player_id
        self.host = host
        logger.info("Generating player {}".format(self.id))
        self.hand = Deck(7, player_id)
        self.skip = False
        self.board:Board = None

    def setNewBoard(self, board:Board):
        logger.info("Associating board {} to player".format(board.name))
        self.board = board

    def play(self):
        update = False
        allowed_card_list:list[Card] = self.getValidCards()

        if allowed_card_list == []:
            player.grab_card(deck)
            selected = None
            update = True
            turn_done = True
            return (update, selected, turn_done)
        
        while not update:
            (update, selected, turn_done) = player_LR_selection_hand(self, None, self.board, allowed_card_list)

        card = self.hand.playCardFromHand(allowed_card_list[selected].id)

        logger.info("Played ({},{},{})".format(card.id, card.properties.type, card.properties.color))

        return (update, card, turn_done)

    def playCard(self, card_id):
        card = self.hand.playCardFromHand(card_id)


    def getValidCards(self):
        allow = []
        board = self.board
        for card in self.hand.cardsList():
            if board.cards == {} or board.color == None:
                allow = range(len(self.hand.cardsList()))
                break
            elif card.canPlayWild() or card.canPlay(board.color, board.type):
                allow.append(card)
        
        return allow

    def grabCards(self, number=1):
        self.hand.getCardFromDeck(number)
        
    
