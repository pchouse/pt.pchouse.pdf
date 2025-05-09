/*
 *  Copyright (C) 2022  PChouse - Reflexão Estudos e Sistemas Informáticos, lda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package pt.pchouse.pdf.api.request.datasource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Represents a JSON-based HTTPS remote datasource.
 * <p>
 * This class extends the {@code AServer} abstract class, inheriting its behavior
 * for managing common server properties such as URL and HTTP request type.
 * It implements {@code IRemoteDatasource}, signifying its purpose as a remote
 * data source. The class is configurable as a Spring component and operates
 * with prototype scope.
 * <p>
 * It is annotated to handle JSON properties in a case-insensitive manner
 * and to ignore unknown properties during deserialization.
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class JsonHttps extends AServer implements IRemoteDatasource
{

    /**
     * Default constructor for the JsonHttps class.
     * <p>
     * This constructor initializes a new instance of the JsonHttps class,
     * invoking the superclass AServer's constructor for initialization.
     * It sets up the foundation for configuring and managing a JSON-based
     * HTTPS remote datasource.
     *
     * @since 1.0.0
     */
    public JsonHttps() {
        super();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        JsonHttps jsonHttps = (JsonHttps) obj;
        return super.equals(jsonHttps); // Compare parent class fields.
    }

}
