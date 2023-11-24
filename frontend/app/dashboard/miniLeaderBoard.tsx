"use client";

import MiniLeaderBoardContent from "./miniLeaderBoardContent";
import { leaderBoardEntry } from "../types/leaderBoardEntry";
import React, { useState, useEffect } from "react";
import { RegionType } from "../types/RegionType";

interface Props {
  heading: string;
  entryFetchFunction: (region: RegionType) => Promise<leaderBoardEntry[]>;
}

export default function MiniLeaderBoard({
  heading,
  entryFetchFunction,
}: Props): JSX.Element {
  const [selectedRegion, setSelectedRegion] = useState(RegionType.Global);
  const [globalEntries, setGlobalEntries] = useState<leaderBoardEntry[]>([]);
  const [continentEntries, setContinentEntries] = useState<leaderBoardEntry[]>(
    []
  );
  const [countryEntries, setCountryEntries] = useState<leaderBoardEntry[]>([]);

  useEffect(() => {
    entryFetchFunction(RegionType.Global)
      .then((entries) => {
        setGlobalEntries(entries);
      })
      .catch(() => {});
    entryFetchFunction(RegionType.Continent)
      .then((entries) => {
        setContinentEntries(entries);
      })
      .catch(() => {});
    entryFetchFunction(RegionType.Country)
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
            selectedRegion === RegionType.Global
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-global"
            aria-controls="tabs-global"
            aria-selected={selectedRegion === RegionType.Global}
            onClick={() => setSelectedRegion(RegionType.Global)}
          >
            Global
          </a>
        </li>
        <li
          role="presentation"
          className={`flex-auto text-center bg-primary ${
            selectedRegion === RegionType.Continent
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-continent"
            aria-controls="tabs-continent"
            aria-selected={selectedRegion === RegionType.Continent}
            onClick={() => setSelectedRegion(RegionType.Continent)}
          >
            Continent
          </a>
        </li>
        <li
          role="presentation"
          className={`flex-auto text-center bg-primary rounded-tr-lg ${
            selectedRegion === RegionType.Country
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-country"
            aria-controls="tabs-country"
            aria-selected={selectedRegion === RegionType.Country}
            onClick={() => setSelectedRegion(RegionType.Country)}
          >
            Country
          </a>
        </li>
      </ul>

      <div className="mb-6">
        <div
          style={{
            display: selectedRegion === RegionType.Global ? "block" : "none",
          }}
          id="tabs-global"
          role="tabpanel"
          aria-labelledby="tabs-global"
          data-te-tab-active
        >
          <MiniLeaderBoardContent title={heading} entries={globalEntries} />
        </div>
        <div
          style={{
            display: selectedRegion === RegionType.Continent ? "block" : "none",
          }}
          id="tabs-continent"
          role="tabpanel"
          aria-labelledby="tabs-continent"
        >
          <MiniLeaderBoardContent title={heading} entries={continentEntries} />
        </div>
        <div
          style={{
            display: selectedRegion === RegionType.Country ? "block" : "none",
          }}
          id="tabs-country"
          role="tabpanel"
          aria-labelledby="tabs-country"
        >
          <MiniLeaderBoardContent title={heading} entries={countryEntries} />
        </div>
      </div>
    </div>
  );
}
