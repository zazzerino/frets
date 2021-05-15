import { sendMessage } from './socket';

export type MessageType =
  'LOGIN'
  | 'CREATE_GAME'
  | 'JOIN_GAME';

export interface Message {
  type: MessageType
}

interface LoginMessage extends Message {
  type: 'LOGIN';
  name: string;
}

function loginMessage(name: string): LoginMessage {
  return {
    type: 'LOGIN',
    name
  }
}

export function sendLogin(name: string) {
  sendMessage(loginMessage(name));
}

interface CreateGameMessage extends Message {
  type: 'CREATE_GAME';
}

function createGameMessage(): CreateGameMessage {
  return {
    type: 'CREATE_GAME'
  }
}

export function sendCreateGame() {
  sendMessage(createGameMessage());
}

interface JoinGameMessage extends Message {
  type: 'JOIN_GAME';
  gameId: number;
  userId: number;
}

function joinGameMessage(gameId: number, userId: number): JoinGameMessage {
  return {
    type: 'JOIN_GAME',
    gameId,
    userId
  }
}

export function sendJoinGame(gameId: number, userId: number) {
  sendMessage(joinGameMessage(gameId, userId));
}
