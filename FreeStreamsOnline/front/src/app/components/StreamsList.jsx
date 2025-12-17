import { StreamCards } from "./StreamCards";

export const StreamsList = ({ streams, onEdit, onDelete }) => {
    return (
        <div className="row">
        {streams.map(s => (
            <div key={s.id} className="col-sm-6 col-lg-4">
            <StreamCards stream={s} onEdit={()=>onEdit(s)} onDelete={()=>onDelete(String(s.id))} />
            </div>
        ))}
        </div>
    );
}