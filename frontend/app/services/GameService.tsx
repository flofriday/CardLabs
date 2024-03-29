import { getCookie } from "cookies-next";
import { Page } from "../types/contentPage";
import { refreshAccessToken } from "./RefreshService";
import { toast } from "react-toastify";
import { Game } from "../types/game";
import { UnAuthorizedError } from "../exceptions/UnAuthorizedError";
import { LogLine } from "../types/LogLine";
import { NotFoundError } from "../exceptions/NotFoundError";

export async function getGames(
  botId: number,
  numberOfEntriesPerPage: number,
  pageNumber: number
): Promise<Page<Game>> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  const response = await fetch(
    `/api/match/bot/${botId}?pageNumber=${pageNumber}&pageSize=${numberOfEntriesPerPage}`,
    {
      mode: "cors",
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwt,
      },
    }
  );

  if (response.status === 200) {
    const page = (await response.json()) as Page<Game>;
    for (let i = 0; i < page.content.length; i++) {
      page.content[i].startTime = new Date(page.content[i].startTime);
      page.content[i].endTime = new Date(page.content[i].endTime);
    }
    return page;
  }

  if (response.status === 401) {
    throw new UnAuthorizedError("Not authorized");
  }

  toast.error("An error occurred. Please try again later.");
  throw Error();
}

export async function getGame(gameID: number): Promise<Game> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  try {
    const response = await fetch(`/api/match/${gameID}/all`, {
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
      throw new NotFoundError("Game not found");
    }

    if (response.status !== 200) {
      throw new Error("Failed to fetch match log");
    }

    const data = (await response.json()) as Game;
    data.startTime = new Date(data.startTime);
    data.endTime = new Date(data.endTime);
    return data;
  } catch (error: any) {
    console.error("Error fetching match log:", error.message);
    throw error;
  }
}

export async function getLogLinesForGame(
  gameID: number,
  botId: number
): Promise<LogLine[]> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  try {
    const response = await fetch(`/api/match/${gameID}/log/${botId}`, {
      mode: "cors",
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwt,
      },
    });

    if (response.status !== 200) {
      throw new Error("Failed to fetch match log");
    }

    const data = (await response.json()) as LogLine[];
    const res: LogLine[] = [];

    for (let i = 0; i < data.length; i++) {
      if (data[i].type.toLowerCase() === "debug" && data[i].botId === botId) {
        res.push(data[i]);
      } else if (data[i].type.toLowerCase() === "system") {
        res.push(data[i]);
      }
    }

    return res;
  } catch (error: any) {
    console.error("Error fetching match log:", error.message);
    throw error;
  }
}
