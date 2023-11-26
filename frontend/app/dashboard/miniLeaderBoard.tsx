"use client";

import MiniLeaderBoardContent from "./miniLeaderBoardContent";
import { leaderBoardEntry } from "../types/leaderBoardEntry";
import React, { useState, useEffect } from "react";
import { RegionType } from "../types/RegionType";
import { LeaderBoardType } from "../types/LeaderBoardType";

interface Props {
  leaderboardType: LeaderBoardType;
  entryFetchFunction: (region: RegionType) => Promise<leaderBoardEntry[]>;
}

export default function MiniLeaderBoard({
  leaderboardType,
  entryFetchFunction,
}: Props): JSX.Element {
  const [selectedRegion, setSelectedRegion] = useState(RegionType.GLOBAL);
  const [globalEntries, setGlobalEntries] = useState<leaderBoardEntry[]>([]);
  const [continentEntries, setContinentEntries] = useState<leaderBoardEntry[]>(
    []
  );
  const [countryEntries, setCountryEntries] = useState<leaderBoardEntry[]>([]);

  useEffect(() => {
    entryFetchFunction(RegionType.GLOBAL)
      .then((entries) => {
        setGlobalEntries(entries);
      })
      .catch(() => {});
    entryFetchFunction(RegionType.CONTINENT)
      .then((entries) => {
        setContinentEntries(entries);
      })
      .catch(() => {});
    entryFetchFunction(RegionType.COUNTRY)
      .then((entries) => {
        setCountryEntries(entries);
      })
      .catch(() => {});
  }, []);

  return (
    <div className="bg-secondary rounded-lg w-full">
      <ul
        className="mb-5 flex list-none flex-row flex-wrap border-b-0 pl-0"
        role="tablist"
        data-te-nav-ref
      >
        <li
          role="presentation"
          className={`flex-auto text-center bg-primary rounded-tl-lg ${
            selectedRegion === RegionType.GLOBAL
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-global"
            aria-controls="tabs-global"
            aria-selected={selectedRegion === RegionType.GLOBAL}
            onClick={() => setSelectedRegion(RegionType.GLOBAL)}
          >
            Global
          </a>
        </li>
        <li
          role="presentation"
          className={`flex-auto text-center bg-primary ${
            selectedRegion === RegionType.CONTINENT
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-continent"
            aria-controls="tabs-continent"
            aria-selected={selectedRegion === RegionType.CONTINENT}
            onClick={() => setSelectedRegion(RegionType.CONTINENT)}
          >
            Continent
          </a>
        </li>
        <li
          role="presentation"
          className={`flex-auto text-center bg-primary rounded-tr-lg ${
            selectedRegion === RegionType.COUNTRY
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-country"
            aria-controls="tabs-country"
            aria-selected={selectedRegion === RegionType.COUNTRY}
            onClick={() => setSelectedRegion(RegionType.COUNTRY)}
          >
            Country
          </a>
        </li>
      </ul>

      <div className="mb-6">
        <div
          style={{
            display: selectedRegion === RegionType.GLOBAL ? "block" : "none",
          }}
          id="tabs-global"
          role="tabpanel"
          aria-labelledby="tabs-global"
          data-te-tab-active
        >
          <MiniLeaderBoardContent
            regionType={RegionType.GLOBAL}
            leaderBoardType={leaderboardType}
            entries={globalEntries}
          />
        </div>
        <div
          style={{
            display: selectedRegion === RegionType.CONTINENT ? "block" : "none",
          }}
          id="tabs-continent"
          role="tabpanel"
          aria-labelledby="tabs-continent"
        >
          <MiniLeaderBoardContent
            regionType={RegionType.CONTINENT}
            leaderBoardType={leaderboardType}
            entries={continentEntries}
          />
        </div>
        <div
          style={{
            display: selectedRegion === RegionType.COUNTRY ? "block" : "none",
          }}
          id="tabs-country"
          role="tabpanel"
          aria-labelledby="tabs-country"
        >
          <MiniLeaderBoardContent
            regionType={RegionType.COUNTRY}
            leaderBoardType={leaderboardType}
            entries={countryEntries}
          />
        </div>
      </div>
    </div>
  );
}
