import { getCookie } from "cookies-next";

export async function getNewBotName(): Promise<string> {
  const jwt = getCookie("auth_token");

  const response = await fetch("/api/bot/name", {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "text/plain",
      Authorization: "Bearer " + jwt,
    },
  });
  return await response.text();
}

export async function createBot(
  name: string,
  currentCode: string | null
): Promise<number> {
  const jwt = getCookie("auth_token");

  const response = await fetch("/api/bot", {
    mode: "cors",
    method: "POST",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      name,
      currentCode,
    }),
  });

  // TODO add error handling

  const data = await response.json();
  return data.id;
}

enum BotState {
  CREATED,
  AWAITING_VERIFICATION,
  READY,
  QUEUED,
  PLAYING,
  ERROR,
}

export interface Bot {
  id: number;
  name: string;
  ownerId: number;
  currentCode: string;
  codeHistory: any; // todo fix this type
  eloScore: number;
  currentState: BotState;
  defaultState: BotState;
  errorStateMessage: string;
}

export async function getBot(id: number): Promise<any> {
  const jwt = getCookie("auth_token");

  const response = await fetch("/api/bot/" + id, {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  // TODO add error handling
  const bot = (await response.json()) as Bot;
  return bot;
}
