import { writable, Writable } from "svelte/store";
import { defaultUser, User } from "./user";
import type { Game } from "./game/game";
import Home from './Home.svelte';

export const page: Writable<typeof Home> = writable(Home);

export const user: Writable<User> = writable(defaultUser);

export const game: Writable<Game> = writable(null);

export const games: Writable<Game[]> = writable([]);

export const players: Writable<User[]> = writable([]);
