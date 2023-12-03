import LeftPageHeader from "@/app/components/leftPageHeader";
import EditorButtons from "./editorButtons";
import LoggingElement from "./loggingElement";
import CodeEditor from "./codeEditor";
export default function BotEditor(): JSX.Element {
  return (
    <div className="w-full h-full flex">
      <LeftPageHeader title="Adath Wacoffin" subTitle="Bot-Name" />
      <div className="h-full w-8/12">
        <EditorButtons />
        <CodeEditor />
      </div>
      <LoggingElement />
    </div>
  );
}
