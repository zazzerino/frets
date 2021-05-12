import router from 'page';
import App from './App.svelte';
import Home from './Home.svelte';
import Login from './Login.svelte';
import { page } from './stores';
import * as _socket from './socket/socket';

function setupRoutes() {
	router('/', () => page.set(Home));
	router('/login', () => page.set(Login));

	router.start();
}

const app = new App({
	target: document.body
});

setupRoutes();

export default app;