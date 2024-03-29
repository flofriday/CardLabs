import { getCookie } from "cookies-next";
import { toast } from "react-toastify";
import { RegionType } from "../types/RegionType";
import { Page } from "../types/contentPage";
import { refreshAccessToken } from "./RefreshService";
import { UnAuthorizedError } from "../exceptions/UnAuthorizedError";
import { NotFoundError } from "../exceptions/NotFoundError";
import { RestAPIError } from "@/app/exceptions/RestAPIError";

export async function getNewBotName(): Promise<string> {
  await refreshAccessToken();
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
    toast.error("An error occurred. Please try again later.");
    throw new EvalError(); // TODO change this
  }

  return await response.text();
}

export async function getTestBots(): Promise<string> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  const response = await fetch("/api/bot/test-bots", {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "text/plain",
      Authorization: "Bearer " + jwt,
    },
  });

  if (response.status !== 200) {
    toast.error(
      "An error occurred fetching the test bots. Please try again later."
    );
    throw new EvalError(); // TODO change this
  }

  return await response.text();
}

export async function createTestMatch(id: number): Promise<number> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");
  const testBotId: number = -1;

  const response = await fetch(
    "/api/bot/" + id + "/test-match/" + testBotId + "?useCurrentCode=true",
    {
      mode: "cors",
      method: "POST",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwt,
      },
    }
  );

  if (response.status !== 200) {
    toast.error(
      "An error occurred creating a test match. Please try again later."
    );
    throw new EvalError(); // TODO change this
  }

  return await response.text().then((str) => Number(str));
}

export async function rankBot(id: number): Promise<void> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  const response = await fetch(`/api/bot/${id}/rank`, {
    mode: "cors",
    method: "POST",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  if (response.status !== 200) {
    if (response.status === 409) {
      toast.success("Successfully queued the bot for ranking");
    } else {
      toast.error("An error occurred ranking the bot. Please try again later.");
      throw new RestAPIError(
        `HTTP REST API error with code ${response.status} occurred`
      );
    }
  }
}

export async function createBot(
  name: string,
  currentCode: string
): Promise<number> {
  await refreshAccessToken();
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
    toast.error("An error occurred. Please try again later.");
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
  codeHistory: CodeHistory[];
  eloScore: number;
  currentState: BotState;
  defaultState: BotState;
  errorStateMessage: string;
  updated: Date;
  created: Date;
}

export interface CodeHistory {
  botId: number;
  code: string;
  id: number;
}

export async function getBotName(
  id: number
): Promise<{ id: number; name: string }> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  const response = await fetch(`/api/bot/${id}/name`, {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  if (response.status === 401 || response.status === 403) {
    throw new UnAuthorizedError("Not authorized");
  }

  if (response.status === 404) {
    throw new NotFoundError("Bot not found");
  }

  if (response.status !== 200) {
    toast.error("An error occurred. Please try again later.");
    throw new Error();
  }
  const name = await response.text();
  return { id, name };
}

export async function getBot(id: number): Promise<Bot> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  const response = await fetch("/api/bot/" + id, {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  if (response.status === 401 || response.status === 403) {
    throw new UnAuthorizedError("Not authorized");
  }

  if (response.status === 404) {
    throw new NotFoundError("Bot not found");
  }

  if (response.status !== 200) {
    toast.error("An error occurred. Please try again later.");
    throw new Error();
  }

  const bot = (await response.json()) as Bot;
  bot.created = new Date(bot.created);
  bot.updated = new Date(bot.updated);
  return bot;
}

export async function saveBot(
  id: number,
  currentCode: string
): Promise<boolean> {
  await refreshAccessToken();
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
    toast.error("An error occurred. Please try again later.");
    return false;
  }
}

export async function deleteBot(id: number): Promise<boolean> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  const response = await fetch("/api/bot/" + id, {
    mode: "cors",
    method: "DELETE",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  if (response.status !== 200) {
    toast.error("An error occurred. Please try again later.");
    throw new EvalError(); // TODO change this
  }

  toast.success("Bot deleted!");

  return true;
}

export async function getAllBots(
  pageNumber: number,
  pageSize: number
): Promise<Page<Bot>> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  // return an empty page if the pageNumber is negative as then there are not bots yet
  if (pageNumber < 0) {
    return {
      first: true,
      empty: true,
      last: true,
      number: 0,
      numberOfElements: 0,
      size: 0,
      totalElements: 0,
      totalPages: 0,
      content: [],
    };
  }
  const response = await fetch(
    "/api/bot?" +
      new URLSearchParams({
        pageNumber: pageNumber.toString(),
        pageSize: pageSize.toString(),
      }).toString(),
    {
      mode: "cors",
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwt,
      },
    }
  );

  if (response.status !== 200) {
    toast.error(
      "An error occurred. Please try again later. If the error persists, please contact the support."
    );
    throw new EvalError(); // TODO change this
  }

  const page = (await response.json()) as Page<Bot>;

  for (let i = 0; i < page.content.length; i++) {
    page.content[i].created = new Date(page.content[i].created);
    page.content[i].updated = new Date(page.content[i].updated);
  }

  return page;
}

export async function getBotRank(
  botId: number,
  region: RegionType
): Promise<number> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  const response = await fetch(
    "/api/bot/" +
      botId +
      "/rank?" +
      new URLSearchParams({
        region: region.toString().toUpperCase(),
      }).toString(),
    {
      mode: "cors",
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwt,
      },
    }
  );

  if (response.status !== 200) {
    toast.error(
      "An error occurred. Please try again later. If the error persists, please contact the support."
    );
    throw new EvalError(); // TODO change this
  }

  return (await response.json()) as number;
}
