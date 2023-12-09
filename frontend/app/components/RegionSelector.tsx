"use client";

import React, { useState } from "react";
import { RegionType } from "../types/RegionType";

interface Props {
  onRegionChange: (region: RegionType) => void;
}

export default function RegionSelector({ onRegionChange }: Props): JSX.Element {
  const [selectedRegion, setSelectedRegion] = useState<RegionType>(
    RegionType.GLOBAL
  );

  const updateEntriesAccordingToRegion = (region: RegionType): void => {
    setSelectedRegion(region);
    onRegionChange(region);
  };

  return (
    <div className="bg-secondary rounded-lg w-full">
      <h1 className="text-4xl font-bold p-6">Region</h1>
      <div className="flex flex-col space-y-4 p-6">
        <button
          className="btn bg-primary py-2 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight"
          onClick={() => {
            updateEntriesAccordingToRegion(RegionType.GLOBAL);
          }}
        >
          Global
        </button>
        <button
          className="btn bg-primary py-2 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight"
          onClick={() => {
            updateEntriesAccordingToRegion(RegionType.CONTINENT);
          }}
        >
          Continent
        </button>
        <button
          className="btn bg-primary py-2 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight"
          onClick={() => {
            updateEntriesAccordingToRegion(RegionType.COUNTRY);
          }}
        >
          Country
        </button>
      </div>
    </div>
  );
}
