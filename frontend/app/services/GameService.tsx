import { getCookie } from "cookies-next";
import { Page } from "../types/contentPage";

export async function getGames(
  botId: number,
  numberOfEntriesPerPage: number,
  pageNumber: number
): Promise<Page<Game>> {
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

  if (response.status == 200) {
    const data = await response.json();
    console.log(data);
    return data as Page<Game>;
  }

  throw Error();
}
