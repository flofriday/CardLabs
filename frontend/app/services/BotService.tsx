import { getCookie } from "cookies-next";
import { toast } from "react-toastify";
import { MatchOverviewEntryType } from "../types/MatchOverviewEntryType";
import { RegionType } from "../types/RegionType";
import { Page } from "../types/contentPage";

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
    toast.error("An error occurred. Please try again later.");
    throw new EvalError(); // TODO change this
  }

  return await response.text();
}

export async function createBot(
  name: string,
  currentCode: string
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
  codeHistory: any; // todo fix this type
  eloScore: number;
  currentState: BotState;
  defaultState: BotState;
  errorStateMessage: string;
  updated: Date;
  created: Date;
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
    toast.error("An error occurred. Please try again later.");
    throw new EvalError(); // TODO change this
  }

  // TODO add error handling
  const bot = (await response.json()) as Bot;
  bot.created = new Date(bot.created);
  bot.updated = new Date(bot.updated);
  return bot;
}

export async function saveBot(
  id: number,
  currentCode: string
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
    toast.error("An error occurred. Please try again later.");
    return false;
  }
}

export async function deleteBot(id: number): Promise<boolean> {
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

export async function getBotMatchOverviewPage(
  numberOfEntriesPerPage: number,
  pageNumber: number
): Promise<MatchOverviewEntryType[]> {
  /* TODO change this to the correct endpoint in the backend
  const url = `api/bot/public?page=${pageNumber}&entriesPerPage=${numberOfEntriesPerPage}`;

  const response = await fetch(url, {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });

  if (!response.ok) {
    throw new Error(`HTTP error! Status: ${response.status}`);
  } else {
    return await response.json();
  }
  */
  if (pageNumber == 1) {
    return [
      {
        gameDate: new Date("2023-01-01T12:00:00"),
        place: 1,
        scoreDevelopment: 1500,
        botVersion: "v1.0",
        gameId: 12345,
      },
      {
        gameDate: new Date("2023-01-02T15:30:00"),
        place: 2,
        scoreDevelopment: 1450,
        botVersion: "v1.1",
        gameId: 67890,
      },
      {
        gameDate: new Date("2023-01-03T18:45:00"),
        place: 3,
        scoreDevelopment: 1600,
        botVersion: "v1.2",
        gameId: 112233,
      },
      {
        gameDate: new Date("2023-01-04T10:15:00"),
        place: 4,
        scoreDevelopment: 1400,
        botVersion: "v1.3",
        gameId: 445566,
      },
      {
        gameDate: new Date("2023-01-05T20:00:00"),
        place: 5,
        scoreDevelopment: 1550,
        botVersion: "v1.4",
        gameId: 778899,
      },
    ];
  } else {
    return [
      {
        gameDate: new Date("2023-01-01T12:00:00"),
        place: 54,
        scoreDevelopment: 1500,
        botVersion: "v1.0",
        gameId: 12345,
      },
      {
        gameDate: new Date("2023-01-02T15:30:00"),
        place: 56,
        scoreDevelopment: 1450,
        botVersion: "v1.1",
        gameId: 67890,
      },
      {
        gameDate: new Date("2023-01-03T18:45:00"),
        place: 101,
        scoreDevelopment: 1600,
        botVersion: "v1.2",
        gameId: 112233,
      },
      {
        gameDate: new Date("2023-01-04T10:15:00"),
        place: 107,
        scoreDevelopment: 1400,
        botVersion: "v1.3",
        gameId: 445566,
      },
      {
        gameDate: new Date("2023-01-05T20:00:00"),
        place: 233,
        scoreDevelopment: 1550,
        botVersion: "v1.4",
        gameId: 778899,
      },
    ];
  }
}

export async function getTotalNumberOfBotMatchOverviewPages(
  entriesPerPage: number
): Promise<number> {
  // TODO replace this with calls to the backend
  return 2;
}

export async function getAllBots(
  pageNumber: number,
  pageSize: number
): Promise<Page<Bot>> {
  const jwt = getCookie("auth_token");

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
