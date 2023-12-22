"use client";

import LeftPageHeader from "@/app/components/leftPageHeader";
import EditorButtons from "./editorButtons";
import LoggingElement from "./loggingElement";
import CodeEditor from "./codeEditor";
import { useEffect, useState } from "react";
import {
  getNewBotName,
  createBot,
  getBot,
  Bot,
  saveBot as _saveBot,
  deleteBot as _deleteBot,
} from "@/app/services/BotService";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

function saveNewBot(
  name: string,
  code: string,
  setId: (id: number) => void,
  router: any
): void {
  createBot(name, code)
    .then((id) => {
      setId(id);
      router.replace("/bot/editor/" + String(id));
    })
    .catch(() => {});
}

function saveBot(id: number, code: string): void {
  _saveBot(id, code)
    .then(() => {})
    .catch(() => {});
}

function deleteBot(id: number, router: any): void {
  _deleteBot(id)
    .then(() => {
      router.replace("/bot");
    })
    .catch(() => {});
}

interface Props {
  id?: number | null;
}
export default function BotEditor({ id = null }: Props): JSX.Element {
  const [_id, setId] = useState<number | null>(id);
  const [name, setName] = useState("");
  const [code, setCode] = useState<string | undefined>(undefined);
  const router = useRouter();

  useEffect(() => {
    if (_id == null) {
      // fetch new name
      getNewBotName()
        .then((n) => {
          setName(n);
        })
        .catch(() => {});
    } else {
      getBot(_id)
        .then((b: Bot) => {
          setName(b.name);
          setCode(b.currentCode);
        })
        .catch(() => {});
    }
  }, [_id]);

  return (
    <div className="w-full h-full flex">
      <LeftPageHeader title={name} subTitle="Bot-Name" />
      <div className="h-full w-11/12">
        <EditorButtons
          save={() => {
            if (_id == null) {
              saveNewBot(name, code ?? "", setId, router);
            } else {
              saveBot(_id, code ?? "");
            }
          }}
          _delete={() => {
            if (_id != null) {
              deleteBot(_id, router);
            } else {
              toast.success("Bot deleted");
              router.replace("/bot");
            }
          }}
        />
        <CodeEditor code={code ?? null} onChange={setCode} />
      </div>
      <LoggingElement />
    </div>
  );
}
