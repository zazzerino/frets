import "./global.postcss"
import App from './App.svelte'
import { setupRoutes } from "./routes"

const app = new App({
  target: document.getElementById('app')
})

setupRoutes();

export default app
