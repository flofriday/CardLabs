"use client";

import { useEffect, useState } from "react";
import LeftPageHeader from "../components/leftPageHeader";
import BotCard from "./botCard";
import { Bot, getAllBots } from "../services/BotService";

export default function BotOverview(): JSX.Element {
  const [page, setPage] = useState(0);
  const [numPages, setNumPages] = useState(0);
  const [bots, setBots] = useState<Bot[]>([]);

  useEffect(() => {
    getAllBots(page, 6)
      .then((page) => {
        setBots(page.content);
        setNumPages(page.totalPages);
      })
      .catch(() => {});
  }, [page]);

  return (
    <>
      <LeftPageHeader title="My Bots" margin={5} />
      <div className="h-full w-full flex items-center justify-center">
        <div className="h-4/6 w-9/12 grid grid-cols-1 lg:grid-cols-2 2xl:grid-cols-3 gap-8">
          {bots.map((b, idx) => {
            return <BotCard key={idx} bot={b} />;
          })}
        </div>
      </div>
    </>
  );
}
