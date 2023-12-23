"use client";

import { notFound } from "next/navigation";
import BotEditor from "../botEditor";
export default function BotWithIdEditor({
  params,
}: {
  params: { slug: string };
}): JSX.Element {
  if (isNaN(Number(params.slug))) {
    return notFound();
  }

  return <BotEditor id={Number(params.slug)} />;
}
