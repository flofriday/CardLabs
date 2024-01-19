import { toast } from "react-toastify";
import { LeaderBoardType } from "../types/LeaderBoardType";
import { RegionType } from "../types/RegionType";
import { Page } from "../types/contentPage";
import { leaderBoardEntry } from "../types/leaderBoardEntry";
import { getUserInfo } from "./UserService";
import { getCookie } from "cookies-next";

export async function getGlobalTop5LeaderBoardEntries(
  regionType: RegionType
): Promise<leaderBoardEntry[]> {
  const jwt = getCookie("auth_token");

  if (jwt === undefined || jwt === null) {
    toast.error(
      "Leaderboard could not be loaded as the account could not be verified. Please trying logging out and logging and repeating your action."
    );
    return {} as const as leaderBoardEntry[];
  }

  let url = `/api/leaderboard/public?page=${0}&entriesPerPage=${5}&regionType=${regionType.toUpperCase()}`;

  if (regionType !== RegionType.GLOBAL) {
    let filter = null;
    const user = await getUserInfo();
    filter = user.location;
    if (filter !== null) {
      url = `${url}&filter=${filter}`;
    } else {
      regionType = RegionType.GLOBAL;
      url = `/api/leaderboard/public?page=${0}&entriesPerPage=${5}&regionType=${regionType.toUpperCase()}`;
    }
  }

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
    const leaderBoardPage = (await response.json()) as Page<leaderBoardEntry>;
    return leaderBoardPage.content;
  }
}

export async function getMyTop5LeaderBoardEntries(
  regionType: RegionType
): Promise<leaderBoardEntry[]> {
  const user = await getUserInfo();
  let url = `/api/leaderboard/private?page=${0}&entriesPerPage=${5}&regionType=${regionType.toUpperCase()}&userId=${
    user.id
  }`;

  const jwt = getCookie("auth_token");

  if (jwt === undefined || jwt === null) {
    toast.error(
      "Leaderboard could not be loaded as the account could not be verified. Please trying logging out and logging and repeating your action."
    );
    return {} as const as leaderBoardEntry[];
  }

  if (regionType !== RegionType.GLOBAL) {
    let filter = null;
    filter = user.location;
    if (filter !== null) {
      url = `${url}&filter=${filter}`;
    } else {
      regionType = RegionType.GLOBAL;
      url = `/api/leaderboard/private?page=${0}&entriesPerPage=${5}&regionType=${regionType.toUpperCase()}&userId=${
        user.id
      }`;
    }
  }

  const response = await fetch(url, {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  if (!response.ok) {
    throw new Error(`HTTP error! Status: ${response.status}`);
  } else {
    const leaderBoardPage = (await response.json()) as Page<leaderBoardEntry>;
    return leaderBoardPage.content;
  }
}

export async function getLeaderBoardPage(
  numberOfEntriesPerPage: number,
  pageNumber: number,
  regionType: RegionType,
  boardType: LeaderBoardType,
  userId?: number
): Promise<Page<leaderBoardEntry>> {
  if (boardType === LeaderBoardType.ALL_BOTS) {
    const loggedIn =
      getCookie("auth_token") !== null && getCookie("auth_token") !== undefined;

    let url = `/api/leaderboard/public?page=${pageNumber}&entriesPerPage=${numberOfEntriesPerPage}&regionType=${regionType.toUpperCase()}`;

    if (!loggedIn) {
      regionType = RegionType.GLOBAL;
      url = `/api/leaderboard/public?page=${pageNumber}&entriesPerPage=${numberOfEntriesPerPage}&regionType=${regionType.toUpperCase()}`;
    } else if (regionType !== RegionType.GLOBAL) {
      let filter = null;
      const user = await getUserInfo();
      filter = user.location;
      if (filter !== null) {
        url = `${url}&filter=${filter}`;
      } else {
        regionType = RegionType.GLOBAL;
        url = `/api/leaderboard/public?page=${pageNumber}&entriesPerPage=${numberOfEntriesPerPage}&regionType=${regionType.toUpperCase()}`;
      }
    }

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
      return (await response.json()) as Page<leaderBoardEntry>;
    }
  } else if (boardType === LeaderBoardType.MY_BOTS) {
    let url = `/api/leaderboard/private?page=${pageNumber}&entriesPerPage=${numberOfEntriesPerPage}&regionType=${regionType.toUpperCase()}&userId=${userId}`;

    const jwt = getCookie("auth_token");

    if (jwt === undefined || jwt === null) {
      toast.error(
        "Leaderboard could not be loaded as the account could not be verified. Please trying logging out and logging and repeating your action."
      );
      return {} as const as Page<leaderBoardEntry>;
    }

    if (regionType !== RegionType.GLOBAL) {
      let filter = null;
      const user = await getUserInfo();
      filter = user.location;
      if (filter !== null) {
        url = `${url}&filter=${filter}`;
      } else {
        regionType = RegionType.GLOBAL;
        url = `/api/leaderboard/private?page=${pageNumber}&entriesPerPage=${numberOfEntriesPerPage}&regionType=${regionType.toUpperCase()}&userId=${userId}`;
      }
    }

    const response = await fetch(url, {
      mode: "cors",
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwt,
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    } else {
      return (await response.json()) as Page<leaderBoardEntry>;
    }
  } else {
    return {} as const as Page<leaderBoardEntry>;
  }
}

export async function getScoreOfGlobalFirstPlace(): Promise<number | undefined> {
  const response = await fetch("api/leaderboard/firstPlace", {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });
  if (response.status === 404) {
    // No need to show an error message as this is the case where not bot exists
    return undefined
  }
  if (response.status !== 200) {
    toast.error(
      "Could not load first place as our servers seem to be too busy. Please try again."
    );
    return undefined;
  }

  return (await response.json()) as number;
}
