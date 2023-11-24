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

export async function getGlobalTop5LeaderBoardEntries(
  regionType: RegionType
): Promise<leaderBoardEntry[]> {
  // TODO replace this with calls to the backend
  if (regionType === RegionType.Global) {
    return exampleEntries;
  } else if (regionType === RegionType.Continent) {
    return exampleEntries1;
  } else if (regionType === RegionType.Country) {
    return exampleEntries2;
  } else {
    return exampleEntries;
  }
}

export async function getMyTop5LeaderBoardEntries(
  regionType: RegionType
): Promise<leaderBoardEntry[]> {
  // TODO replace this with calls to the backend
  if (regionType === RegionType.Global) {
    return exampleEntries;
  } else if (regionType === RegionType.Continent) {
    return exampleEntries1;
  } else if (regionType === RegionType.Country) {
    return exampleEntries2;
  } else {
    return exampleEntries;
  }
}
