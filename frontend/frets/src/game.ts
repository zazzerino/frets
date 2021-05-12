import type { User } from './user';

export type State = 'INIT' | 'PLAYING' | 'ROUND_OVER' | 'GAME_OVER';

export interface Game {
  state: State;
  users: User[];
  roundCount: number;
}

export interface Summary {
  id: number;
  createdAt: string;
  playerCount: number;
  state: State;
}
