import type { User } from "src/user/user";

export type State = 'INIT' | 'PLAYING' | 'ROUND_OVER' | 'GAME_OVER';

export interface Game {
  id: number;
  createdAt: string;
  hostId: number;
  state: State;
  users: User[];
  // playerIds: number[];
  // roundCount: number;
  // stringsToUse: number[];
  // accidentalsToUse: string[];
}

export function playerCount(game: Game): number {
  return game.users.length;
}

export function formatTimestamp(timestamp: string) {
  return new Date(timestamp).toLocaleString();
}

export function formatState(state: State) {
  return state.toLowerCase().replace('_', ' ');
}

export interface Note {
  whiteKey: string;
  accidental: string;
  octave: string;
}
