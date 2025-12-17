import { Link } from "react-router-dom";

export const NotFoundPage = () => {
    return (
        <>
            <h5>Страница не найдена</h5>
            <Link className="nav-link" to="/">
                Вернуться на главную
            </Link>
        </>
    );
};
