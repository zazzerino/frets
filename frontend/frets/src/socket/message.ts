import { sendMessage } from './socket';

export type MessageType =
  'LOGIN'
  | 'CREATE_GAME';

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
