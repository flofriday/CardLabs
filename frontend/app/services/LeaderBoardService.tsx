import { LeaderBoardType } from "../types/LeaderBoardType";
import { RegionType } from "../types/RegionType";
import { leaderBoardEntry } from "../types/leaderBoardEntry";

// TODO: Remove this as this is just mocked data to test the frontend
const exampleEntries: leaderBoardEntry[] = [
  { place: 1, score: 90, botName: "Lewisbot", userName: "User1" },
  { place: 2, score: 85, botName: "Goropogo", userName: "User2" },
  { place: 3, score: 80, botName: "Greenpop", userName: "User3" },
  { place: 4, score: 75, botName: "ThisIsAnAwesome name", userName: "User4" },
  { place: 5, score: 70, botName: "Bot5", userName: "User5" },
];

const exampleEntries1: leaderBoardEntry[] = [
  { place: 1, score: 95, botName: "BotA", userName: "UserA" },
  { place: 2, score: 88, botName: "BotB", userName: "UserB" },
  { place: 3, score: 82, botName: "BotC", userName: "UserC" },
  { place: 4, score: 76, botName: "BotD", userName: "UserD" },
  { place: 5, score: 70, botName: "BotE", userName: "UserE" },
];

// Example Entry List 2
const exampleEntries2: leaderBoardEntry[] = [
  { place: 1, score: 89, botName: "BotX", userName: "UserX" },
  { place: 2, score: 83, botName: "BotY", userName: "UserY" },
  { place: 3, score: 77, botName: "BotZ", userName: "UserZ" },
  { place: 4, score: 71, botName: "BotW", userName: "UserW" },
  { place: 5, score: 65, botName: "BotV", userName: "UserV" },
];

const testentries1: leaderBoardEntry[] = [
  { place: 1, score: 100, botName: "Bot1", userName: "User1" },
  { place: 2, score: 90, botName: "Bot2", userName: "User2" },
  { place: 3, score: 100, botName: "Bot1", userName: "User1" },
  { place: 4, score: 90, botName: "Bot2", userName: "User2" },
  { place: 5, score: 90, botName: "Bot2", userName: "User2" },
];

const testentries2: leaderBoardEntry[] = [
  { place: 6, score: 100, botName: "Bot1", userName: "User1" },
  { place: 7, score: 90, botName: "Bot2", userName: "User2" },
  { place: 8, score: 100, botName: "Bot1", userName: "User1" },
  { place: 9, score: 90, botName: "Bot2", userName: "User2" },
  { place: 10, score: 90, botName: "Bot2", userName: "User2" },
];

const testentries3: leaderBoardEntry[] = [
  { place: 11, score: 1, botName: "Anton", userName: "User1234" },
  { place: 12, score: 2, botName: "Bernd", userName: "User2345" },
  { place: 13, score: 3, botName: "Anna", userName: "User1111" },
  { place: 14, score: 4, botName: "Lisa", userName: "User2222" },
  { place: 15, score: 5, botName: "Kat", userName: "User2468" },
];

const error_entries: leaderBoardEntry[] = [
  { place: 0, score: 0, botName: "0", userName: "0" },
  { place: 0, score: 0, botName: "0", userName: "0" },
  { place: 0, score: 0, botName: "0", userName: "0" },
  { place: 0, score: 0, botName: "0", userName: "0" },
  { place: 0, score: 0, botName: "0", userName: "0" },
];

export async function getGlobalTop5LeaderBoardEntries(
  regionType: RegionType
): Promise<leaderBoardEntry[]> {
  // TODO replace this with calls to the backend
  if (regionType === RegionType.GLOBAL) {
    return exampleEntries;
  } else if (regionType === RegionType.CONTINENT) {
    return exampleEntries1;
  } else if (regionType === RegionType.COUNTRY) {
    return exampleEntries2;
  } else {
    return exampleEntries;
  }
}

export async function getMyTop5LeaderBoardEntries(
  regionType: RegionType
): Promise<leaderBoardEntry[]> {
  // TODO replace this with calls to the backend
  if (regionType === RegionType.GLOBAL) {
    return exampleEntries;
  } else if (regionType === RegionType.CONTINENT) {
    return exampleEntries1;
  } else if (regionType === RegionType.COUNTRY) {
    return exampleEntries2;
  } else {
    return exampleEntries;
  }
}

export async function getLeaderBoardPage(
  numberOfEntriesPerPage: number,
  pageNumber: number,
  regionType: RegionType,
  boardType: LeaderBoardType
): Promise<leaderBoardEntry[]> {
  if (boardType === LeaderBoardType.ALL_BOTS) {
    const url = `api/leaderboard/public?page=${pageNumber}&entriesPerPage=${numberOfEntriesPerPage}&regionType=${regionType}`;

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
  } else {
    // TODO replace this with calls to the backend
    if (regionType === RegionType.CONTINENT) {
      if (pageNumber === 1) {
        return testentries1;
      } else {
        return testentries2;
      }
    } else if (regionType === RegionType.COUNTRY) {
      if (pageNumber === 1) {
        return exampleEntries;
      } else {
        return exampleEntries1;
      }
    } else if (regionType === RegionType.GLOBAL) {
      if (pageNumber === 1) {
        return exampleEntries2;
      } else {
        return testentries3;
      }
    } else {
      return error_entries;
    }
  }
}

export async function getTotalNumberOfPages(
  entriesPerPage: number
): Promise<number> {
  // TODO replace this with calls to the backend
  return 2;
}
