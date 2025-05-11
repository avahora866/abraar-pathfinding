import './RoutesCardGroup.css';

export default function RoutesCardGroup({ routes, reset }) {
    const changeRoute = (e) => {
        let replaced = routes.map((x, i) => {
            return { ...x, selected: i === e };
        });
        reset(replaced)
    }

    return (
        <div className="card-group">
            {routes.map((route, index) => (
                <div key={index} className="card">
                    <div className="card-title">{route.name}</div>
                    <div className="card-content">
                        <p>Time: {route.time}</p>
                        <p>Distance: {route.distance}</p>
                        <button onClick={() => changeRoute(index)}>Change Route</button>
                    </div>
                </div>
            ))}
        </div>
    );
}
