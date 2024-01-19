import { getCookie } from "cookies-next";
import { Page } from "../types/contentPage";
import { refreshAccessToken } from "./RefreshService";
import { toast } from "react-toastify";
import { Game } from "../types/game";
import { UnAuthorizedError } from "../exceptions/UnAuthorizedError";

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
