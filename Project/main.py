import random
import logging
import sys
import argparse

from uno.game import Game
from uno.config import get_config

from uno.pygame.display import redraw_hand_visble


logger = logging.getLogger(__name__)

def setupLogger():
    root_logger = logging.getLogger()
    ch = logging.StreamHandler()
    formatter = logging.Formatter(
        '%(asctime)s, %(levelname)s (%(name)s): %(message)s')
    ch.setFormatter(formatter)
    root_logger.addHandler(ch)

    logging.TRACE = 9
    logging.addLevelName(logging.TRACE, 'TRACE')

    def trace(self, message, *args, **kwargs):
        if self.isEnabledFor(logging.TRACE):
            self._log(logging.TRACE, message, args, **kwargs)

    logging.Logger.trace = trace



def main():
    setupLogger()
    config = get_config()


    parser = argparse.ArgumentParser(
        prog='P2Puno',
        description=' Peer to peer multiplayer UNO.')

    parser.add_argument(
        '--host', '-H',
        metavar='<nfd_host>',
        help='Address of the host to connect to.',
        default=config.get('main', 'host', fallback='localhost'))

    parser.add_argument(
        '--game', '-g',
        metavar='<game_id>',
        help='Set the game id to connect to.',
        default=config.getint(
            'main', 'game_id', fallback=random.getrandbits(64)),
        type=int)

    parser.add_argument(
        '--peer', '-p',
        metavar='<peer_id>',
        help='Set the peer id.',
        default=config.getint(
            'main', 'peer_id', fallback=random.getrandbits(64)),
        type=int)

    parser.add_argument(
        '-v',
        action='count',
        help='Log verbosity from 0 to 5, default to 3')

    args = parser.parse_args()


    loglevels = [logging.CRITICAL,
                 logging.ERROR,
                 logging.WARNING,
                 logging.INFO,
                 logging.DEBUG,
                 logging.TRACE]
    logging.getLogger().setLevel(loglevels[
        config.getint('main', 'log_verbosity', fallback=3)
        if args.v is None else args.v])

    logger.info('Starting app with game_id=%s and peer_id=%s',
                args.game, args.peer)

    game = Game( args.host, args.game, args.peer)
    
    redraw_hand_visble(game.player, None)

    game.run()

if __name__ == "__main__":
    main()
