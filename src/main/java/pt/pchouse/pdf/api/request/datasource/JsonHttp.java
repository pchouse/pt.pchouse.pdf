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
 * Represents a JSON-based HTTP server data source.
 * <p>
 * This class extends the base functionality provided by {@link AServer}
 * and implements the {@link IRemoteDatasource} interface. It provides
 * functionality specific to interacting with a JSON-based remote
 * HTTP server. Properties are configured to handle case-insensitivity
 * during JSON deserialization and to ignore unknown JSON properties.
 * <p>
 * The scope is defined as prototype, meaning a new instance is provided
 * each time it is requested.
 * <p>
 * An instance of this class can be used as a component in a Spring
 * application context due to the {@code @Component} annotation.
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class JsonHttp extends AServer implements IRemoteDatasource
{

    /**
     * Default constructor for the JsonHttp class.
     * Initializes a new instance of the JsonHttp class by invoking the
     * constructor of the parent class {@link AServer}. This sets up the base
     * functionality needed to interact with a JSON-based HTTP server, such as
     * logging and initial property configurations.
     *
     * @since 1.0.0
     */
    public JsonHttp() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        JsonHttp jsonHttp = (JsonHttp) obj;
        return super.equals(jsonHttp); // Compare parent class fields.
    }

}
