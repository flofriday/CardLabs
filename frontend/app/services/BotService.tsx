import { getCookie } from "cookies-next";
import { toast } from "react-toastify";

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

  if (response.status !== 200) {
    toast.error(
      "An error occurred. Please try again later. If the error persists, please contact the support."
    );
    throw new EvalError(); // TODO change this
  }

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

  if (response.status === 201) {
    toast.success("Bot created and saved!");
    const data = await response.json();
    return data.id;
  } else {
    toast.error(
      "An error occurred. Please try again later. If the error persists, please contact the support."
    );
    throw new EvalError(); // TODO change this
  }
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

export async function getBot(id: number): Promise<Bot> {
  const jwt = getCookie("auth_token");

  const response = await fetch("/api/bot/" + id, {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  if (response.status !== 200) {
    toast.error(
      "An error occurred. Please try again later. If the error persists, please contact the support."
    );
    throw new EvalError(); // TODO change this
  }

  // TODO add error handling
  const bot = (await response.json()) as Bot;
  return bot;
}

export async function saveBot(
  id: number,
  currentCode: string | null
): Promise<boolean> {
  const jwt = getCookie("auth_token");

  const response = await fetch("/api/bot/" + id, {
    mode: "cors",
    method: "PATCH",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      currentCode,
    }),
  });

  if (response.status === 200) {
    toast.success("Code saved");
    return true;
  } else {
    toast.error(
      "An error occurred. Please try again later. If the error persists, please contact the support."
    );
    return false;
  }
}
