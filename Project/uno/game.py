import pygame, random, json, logging

from uno.player import Player
from uno.board import Board

from uno.objects.card import Card

from util import convert2serialize

logger = logging.getLogger(__name__)

class Game:
    def __init__(self, host, game_id, peer_id):
        self.host = host
        self.game_id = game_id
        self.peer_id = peer_id

        self.maxPlayerPerGame = 4
        self.player = Player(self.peer_id, self.host)
        self.board:Board = Board("board_"+str(game_id))
        self.player.setNewBoard(self.board)
        self.board.turn_iterator = 1
        self.turn = 0
        self.turn_tot = 0

        self.leader = True

        # modify when implement p2p
        logger.info("Generating fake players")
        self.players:list = [self.player]
        self._setFakeGame()

        self.playersWaiting:list = []

        self.gameRoomSize = len(self.players)
        self.player_turn = self.players[self.turn]

    def _newFakePlayer(self) -> Player:
        return Player(random.getrandbits(64), self.host)

    def _setFakeGame(self):
        for _ in range(3):
            self.players.append(self._newFakePlayer().setNewBoard(self.board))

    def fullRoom(self):
        if self.gameRoomSize == self.maxPlayerPerGame:
            return True

        return False

    def newPlayer(self, player:Player):
        if self.fullRoom():
            self.playersWaiting.append(player)
        else:
            self.players.append(player)
            self.gameRoomSize += 1
        
    def removePlayer(self, player_id):
        p = (player for player in self.players if player.id == player_id)
        self.players.remove(p)

    def promoteWaitingPlayer(self):
        if not self.fullRoom() and len(self.playersWaiting) > 0:
            player:Player = self.playersWaiting.pop(0)
            self.newPlayer(player)

    def getGameState(self, to_json=False):
        if to_json:
            return json.dumps(convert2serialize(self))
        return convert2serialize(self)

    def run(self):
        
        while True:
            turn = self.turn
            self.turn_tot = 0
            self.player_turn = self.players[turn]

            if self.player.skip:
                logger.info("Skipping player turn")
                self.player.skip = False
            else:
                self.player.play()
                
        


    