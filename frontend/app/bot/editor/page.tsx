"use client";

import LeftPageHeader from "@/app/components/leftPageHeader";
import EditorButtons from "./editorButtons";
import LoggingElement from "./loggingElement";
import CodeEditor from "./codeEditor";
import { useEffect, useState } from "react";
import { getNewBotName } from "@/app/services/BotService";

interface Props {
  id?: number;
}

export default function BotEditor({ id }: Props): JSX.Element {
  const [_id, setId] = useState(id);
  const [name, setName] = useState("");

  useEffect(() => {
    if (_id == null) {
      // fetch new name
      getNewBotName()
        .then((n) => {
          setName(n);
        })
        .catch(() => {});
    } else {
      // fetch bot
      setName("Name 2");
    }
  }, [_id]);

  return (
    <div className="w-full h-full flex">
      <LeftPageHeader title={name} subTitle="Bot-Name" />
      <div className="h-full w-8/12">
        <EditorButtons />
        <CodeEditor />
      </div>
      <LoggingElement />
    </div>
  );
}
