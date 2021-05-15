import type { Message } from './message';
import { 
  handleLogin, Response, LoginResponse, handleCreateGame, CreateGameResponse, handleGames, GamesResponse
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
    case 'LOGIN': handleLogin(response as LoginResponse);
    case 'CREATE_GAME': handleCreateGame(response as CreateGameResponse);
    case 'GAMES': handleGames(response as GamesResponse);
  }
}

function onClose(event: CloseEvent) {
  console.log('websocket connection closed...');
}

function onError(_event: Event) {
  console.log('WHOOPS: websocket error...');
}
