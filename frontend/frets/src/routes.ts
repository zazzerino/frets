import router from 'page';
import { page } from './stores';
import HomePage from './HomePage.svelte';
import LoginPage from './user/LoginPage.svelte';
import GamePage from './game/GamePage.svelte';

export function setupRoutes() {
  router('/', () => page.set(HomePage));
  router('/login', () => page.set(LoginPage));
  router('/game', () => page.set(GamePage));
  router.start();
}
