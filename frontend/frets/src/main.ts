import router from 'page';
import App from './App.svelte';
import Home from './Home.svelte';
import Login from './Login.svelte';
import GamePage from './game/GamePage.svelte';
import { page } from './stores';

function setupRoutes() {
	router('/', () => page.set(Home));
	router('/login', () => page.set(Login));
	router('/game', () => page.set(GamePage));
	router.start();
}

const app = new App({
	target: document.body
});

setupRoutes();

export default app;
