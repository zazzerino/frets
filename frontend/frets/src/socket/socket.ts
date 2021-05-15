export const foo = 42;

import type { Message } from './message';
import { 
  handleLogin, Response, LoginResponse, handleCreateGame, CreateGameResponse, handleGames, GamesResponse, handleJoinGame, JoinGameResponse, handleGameUpdated, GameUpdatedResponse
} from './response';

export function sendMessage(message: Message) {
  socket.send(JSON.stringify(message));
}

const WEBSOCKET_URL = 'ws://localhost:8080/ws';

const socket = makeSocket();

function makeSocket() {
  const socket = new WebSocket(WEBSOCKET_URL);
  socket.onopen = onOpen;
  socket.onmessage = onMessage;
  socket.onclose = onClose;
  socket.onerror = onError;
  return socket;
}

function onOpen() {
  console.log('connected to websocket...');
}

function onMessage(event: MessageEvent) {
  const response = JSON.parse(event.data) as Response;
  console.log('received response: ');
  console.log(response);

  switch (response.type) {
    case 'LOGIN': return handleLogin(response as LoginResponse);
    case 'CREATE_GAME': return handleCreateGame(response as CreateGameResponse);
    case 'GAMES': return handleGames(response as GamesResponse);
    case 'GAME_UPDATED': return handleGameUpdated(response as GameUpdatedResponse);
    case 'JOIN_GAME': return handleJoinGame(response as JoinGameResponse);
  }
}

function onClose(_event: CloseEvent) {
  console.log('websocket connection closed...');
}

function onError(_event: Event) {
  console.log('WHOOPS: websocket error...');
}
