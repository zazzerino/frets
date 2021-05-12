import type { User } from '../user';
import { user, game, games } from '../stores';
import type { Game, Summary } from '../game';

export type ResponseType =
  'LOGIN'
  | 'CREATE_GAME'
  | 'GAMES';

export interface Response {
  type: ResponseType;
}

export interface LoginResponse extends Response {
  type: 'LOGIN';
  user: User;
}

export function handleLogin(response: LoginResponse) {
  user.set(response.user);
}

export interface CreateGameResponse extends Response {
  type: 'CREATE_GAME';
  game: Game;
}

export function handleCreateGame(response: CreateGameResponse) {
  game.set(response.game);
}

export interface GamesResponse extends Response {
  type: 'GAMES';
  games: Summary[];
}

export function handleGames(response: GamesResponse) {
  games.set(response.games);
}
