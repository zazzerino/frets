export type State = 'INIT' | 'PLAYING' | 'ROUND_OVER' | 'GAME_OVER';

export interface Game {
  id: number;
  createdAt: string;
  state: State;
  playerIds: number[];
  roundCount: number;
  stringsToUse: number[];
  accidentalsToUse: string[];
}

export function playerCount(game: Game): number {
  return game.playerIds.length;
}

export function formatTimestamp(timestamp: string) {
  return new Date(timestamp).toLocaleString();
}

export function formatState(state: State) {
  return state.toLowerCase().replace('_', ' ');
}
