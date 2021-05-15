export type State = 'INIT' | 'PLAYING' | 'ROUND_OVER' | 'GAME_OVER';

export interface Game {
  id: number;
  state: State;
  playerIds: number[];
  roundCount: number;
}
