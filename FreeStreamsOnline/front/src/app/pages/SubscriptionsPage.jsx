export const SubscriptionsPage = () => {
    return (
        <main className="flex-grow-1 pt-2">
            <h2>Ваши подписки <i className="bi bi-bookmark-heart-fill"></i></h2>
            <ol>
                <li>НОРМ канал</li>
                <div className="subButtons d-flex"><div className="button">Вы подписаны</div></div>
                <li>САМЫЙ КРУТОЙ КАНАЛ</li>
                <div className="subButtons d-flex"><div className="button blue-button">Вы спонсор</div></div>
                <li>ПРОСТО КРУТОЙ канал</li>
                <div className="subButtons d-flex"><div className="button">Вы подписаны</div></div>
            </ol>
        </main>
    )
}