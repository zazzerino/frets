import router from 'page';
import { page } from './stores';
import HomePage from './HomePage.svelte';
import LoginPage from './LoginPage.svelte';

export function setupRoutes() {
  router('/', () => page.set(HomePage));
  router('/login', () => page.set(LoginPage));
  router.start();
}
