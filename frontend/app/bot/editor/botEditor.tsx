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
  const [saved, setSaved] = useState(true);
  const router = useRouter();

  const setupBotCodeTemplate = async (): Promise<string> => {
    try {
      const response = await fetch("../codeTemplate.txt");
      if (!response.ok) {
        console.error(response);
        throw Error();
      }
      return await response.text();
    } catch (error) {
      toast.error("Failed to load new code template");
      return "";
    }
  };

  useEffect(() => {
    if (_id == null) {
      // fetch new name

      getNewBotName()
        .then((n) => {
          setName(n);
          setupBotCodeTemplate().then((codeTemplate: string) => {
            setCode(codeTemplate);
          });
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

  useEffect(() => {
    const handleBeforeUnload = (event: BeforeUnloadEvent): void => {
      if (!saved) {
        event.preventDefault();
        event.returnValue = "";
      }
    };
    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [saved]);

  return (
    <div className="w-full h-full flex">
      <LeftPageHeader title={name} subTitle="Bot-Name" />
      <div className="h-full w-11/12">
        <EditorButtons
          save={() => {
            setSaved(true);
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
        <CodeEditor
          code={code ?? null}
          onChange={(c) => {
            if (c !== code) {
              setSaved(false);
            }
            setCode(c ?? "");
          }}
        />
      </div>
      <LoggingElement />
    </div>
  );
}
